import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DetailComponent } from './detail.component';
import { MatIconModule } from '@angular/material/icon';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockRouter: any;
  let mockSnackBar: any;
  let mockActivatedRoute: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  beforeEach(async () => {
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(
        of({
          id: 1,
          name: 'Test Session',
          teacher_id: 1,
          users: [1],
        })
      ),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({})),
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(
        of({
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
        })
      ),
    };

    mockRouter = {
      navigate: jest.fn(),
    };

    mockSnackBar = {
      open: jest.fn(),
    };

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]), // Utilisation de RouterTestingModule avec des routes fictives
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }, // Injection du mock pour ActivatedRoute
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    component.ngOnInit();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1'); // Vérifie que l'id de session est bien passée
    expect(mockTeacherService.detail).toHaveBeenCalledWith('1'); // Vérifie que l'id de l'enseignant est bien passée
    expect(component.session?.name).toBe('Test Session');
    expect(component.teacher?.firstName).toBe('John');
  });

  it('should delete session and navigate to sessions list', () => {
    component.delete();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1'); // Vérifie que la suppression est appelée avec l'id de session
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
  it('should allow user to participate in a session', () => {
    component.isParticipate = false; // Simule que l'utilisateur n'est pas encore inscrit
    component.participate(); // Appel de la méthode participate

    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1'); // Vérifie que la méthode participe est appelée avec les bons arguments
    expect(mockSessionApiService.detail).toHaveBeenCalled(); // Vérifie que les détails de la session sont à nouveau récupérés après la participation
  });
  it('should allow user to unParticipate from a session', () => {
    component.isParticipate = true; // Simule que l'utilisateur est déjà inscrit
    component.unParticipate(); // Appel de la méthode unParticipate

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1'); // Vérifie que la méthode unParticipate est appelée avec les bons arguments
    expect(mockSessionApiService.detail).toHaveBeenCalled(); // Vérifie que les détails de la session sont à nouveau récupérés après l'annulation de la participation
  });
});
