import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from '../../services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let mockRouter: any;

  beforeEach(async () => {
    // Mock Router
    mockRouter = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        AuthService,
        SessionService,
        { provide: Router, useValue: mockRouter },
      ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);

    fixture.detectChanges();

    // Mock the AuthService login method
    jest.spyOn(authService, 'login').mockReturnValue(
      of({
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'testUser',
        firstName: 'Test',
        lastName: 'User',
        admin: true, // Assurez-vous que cette propriété existe dans l'interface SessionInformation
      } as SessionInformation)
    );
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService login when submitting the form', () => {
    component.form.controls['email'].setValue('yoga@studio.com');
    component.form.controls['password'].setValue('test!1234');
    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'yoga@studio.com',
      password: 'test!1234',
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true if login fails', () => {
    // Override the login method to throw an error
    jest
      .spyOn(authService, 'login')
      .mockReturnValue(throwError(() => ({ message: 'Invalid credentials' })));

    component.form.controls['email'].setValue('wrong@example.com');
    component.form.controls['password'].setValue('wrongpassword');
    component.submit();

    expect(component.onError).toBe(true);
  });

  it('should mark the form as invalid if fields are missing', () => {
    component.submit();
    expect(component.form.invalid).toBeTruthy();
  });
});
