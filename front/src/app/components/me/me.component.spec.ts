import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { UserService } from '../../services/user.service';
import { SessionService } from '../../services/session.service';
import { MeComponent } from './me.component';
import { expect } from '@jest/globals';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockUserService: any;
  let mockRouter: any;
  let mockSnackBar: any;

  const mockSessionService: {
    sessionInformation: { admin: boolean; id: number }; // Permet que sessionInformation soit undefined
    logOut: jest.Mock<any, any>;
  } = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(),
  };

  beforeEach(async () => {
    mockUserService = {
      getById: jest.fn().mockReturnValue(
        of({
          id: 1,
          email: 'test@example.com',
          firstName: 'Test',
          lastName: 'User',
          admin: true,
          password: 'hashed_password',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-06-01T00:00:00Z'),
        } as User)
      ),
      delete: jest.fn().mockReturnValue(of({})),
    };

    mockRouter = {
      navigate: jest.fn(),
    };

    mockSnackBar = {
      open: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user details on init', () => {
    // Vérifie que les détails de l'utilisateur sont bien récupérés au démarrage
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user?.email).toBe('test@example.com');
    expect(component.user?.firstName).toBe('Test');
  });

  it('should call history.back() when back method is called', () => {
    // Simule le retour en arrière
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should delete user and navigate to home on delete', () => {
    // Simule la suppression de l'utilisateur
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
