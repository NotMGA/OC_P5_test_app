import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log out a user and clear session information', () => {
    // On log d'abord un utilisateur pour simuler une session active
    const mockSessionInformation: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBe(true);

    // Test du logout
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit the correct value for $isLogged observable', (done) => {
    // Souscription à l'observable $isLogged pour vérifier les valeurs émises
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });

    // Connexion d'un utilisateur pour activer l'état "isLogged"
    const mockSessionInformation: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    service.logIn(mockSessionInformation);
  });
});
