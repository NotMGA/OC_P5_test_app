import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
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
import { NgZone } from '@angular/core';
import { ListComponent } from 'src/app/features/sessions/components/list/list.component';

describe('LoginComponent - Integration Test', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let ngZone: NgZone;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent, ListComponent],
      providers: [AuthService, SessionService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: ListComponent },
        ]),
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
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService login and navigate to /sessions on successful login', async () => {
    const navigateSpy = jest.spyOn(router, 'navigate');

    // Définir des valeurs valides pour le formulaire
    component.form.patchValue({
      email: 'yoga@studio.com',
      password: 'test!1234',
    });

    ngZone.run(() => {
      component.submit();
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
    });

    await fixture.whenStable();

    // Vérifier que la navigation a été appelée vers '/sessions'
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true if login fails', async () => {
    // Définir des valeurs valides pour le formulaire
    component.form.patchValue({
      email: 'wrong@example.com',
      password: 'wrongpassword',
    });

    ngZone.run(() => {
      // Appeler la méthode submit pour envoyer le formulaire
      component.submit();
    });

    // Simuler la requête HTTP qui échoue
    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush('Invalid credentials', {
      status: 401,
      statusText: 'Unauthorized',
    });

    // Attendre que les modifications se stabilisent avant de vérifier la valeur d'onError
    await fixture.whenStable();

    // Vérifier que onError est défini à true
    expect(component.onError).toBe(true);
  });

  it('should mark the form as invalid if required fields are missing', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    component.submit();
    expect(component.form.invalid).toBeTruthy();
  });
});
