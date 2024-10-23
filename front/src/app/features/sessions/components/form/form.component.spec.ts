import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionApiService: any;
  let mockRouter: any;
  let mockSnackBar: any;
  let mockTeacherService: any;
  let mockActivatedRoute: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(async () => {
    // Mock des services
    mockSessionApiService = {
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(
        of({
          name: 'Test Session',
          date: '2024-01-01',
          teacher_id: '123',
          description: 'A test session',
        })
      ),
    };

    mockRouter = {
      navigate: jest.fn(),
      url: '/create', // Par défaut, on teste en mode "création"
    };

    mockSnackBar = {
      open: jest.fn(),
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([])),
    };

    // Mock de l'ActivatedRoute pour les tests en mode update
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('123'), // ID fictive pour l'update
        },
      },
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]), // Utilisation de RouterTestingModule avec des routes vides
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter }, // Injection du mock du Router
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }, // Mock de l'ActivatedRoute
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form in create mode', () => {
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.controls['name']).toBeDefined();
    expect(component.sessionForm?.controls['date']).toBeDefined();
    expect(component.sessionForm?.controls['teacher_id']).toBeDefined();
    expect(component.sessionForm?.controls['description']).toBeDefined();
  });

  it('should initialize the form in update mode', () => {
    mockRouter.url = '/update/123'; // Simule l'URL pour l'update
    component.ngOnInit();
    expect(component.onUpdate).toBe(true);
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(component.sessionForm?.value.name).toBe('Test Session');
  });

  it('should call create method on submit in create mode', () => {
    component.onUpdate = false;
    component.sessionForm?.patchValue({
      name: 'New Session',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'A new session',
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'New Session',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'A new session',
    });
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call update method on submit in update mode', () => {
    component.onUpdate = true;
    component['id'] = '123';
    component.sessionForm?.patchValue({
      name: 'Updated Session',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'An updated session',
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('123', {
      name: 'Updated Session',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'An updated session',
    });
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Session updated !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should redirect to /sessions if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });
});
