import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { LoginComponent } from '../login/login.component';
import { expect } from '@jest/globals';
import { importProvidersFrom } from '@angular/core';

describe('RegisterComponent - Integration Test', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes([
          { path: 'login', component: LoginComponent },
        ]),
      ],
      providers: [AuthService],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty controls', () => {
    expect(component.form).toBeDefined();
    expect(component.form.controls['email'].value).toBe('');
    expect(component.form.controls['firstName'].value).toBe('');
    expect(component.form.controls['lastName'].value).toBe('');
    expect(component.form.controls['password'].value).toBe('');
  });

  it('should mark form as invalid if required fields are missing', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('');
    component.form.controls['password'].setValue('');
    expect(component.form.invalid).toBeTruthy();
  });

  it('should call AuthService register and navigate to /login on successful registration', async () => {
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.patchValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'test1234',
    });

    component.submit();

    // Simuler la requête HTTP attendue
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush({});

    // Attendre que les modifications se stabilisent avant de vérifier la navigation
    await fixture.whenStable();

    // Vérifier que la navigation a été appelée vers '/login'
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true if registration fails', async () => {
    component.form.patchValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'test1234',
    });

    // Appeler la méthode submit pour envoyer le formulaire
    component.submit();

    // Simuler la requête HTTP qui échoue
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush('Registration failed', {
      status: 400,
      statusText: 'Bad Request',
    });

    // Attendre que les modifications se stabilisent avant de vérifier la valeur d'onError
    await fixture.whenStable();

    // Vérifier que onError est défini à true
    expect(component.onError).toBe(true);
  });
});
