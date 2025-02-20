import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiLoginUrl = `${environment.apiUrl}/auth/login`;
  private apiRegisterUrl = `${environment.apiUrl}/auth/register`;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<{ token: string; role: string }> {
    const loginData = { email, password };
    return this.http.post<{ token: string; role: string }>(this.apiLoginUrl, loginData);
  }

  getToken(): string | null {
    const token = localStorage.getItem('auth_token');
    const expirationTime = localStorage.getItem('auth_token_expiration');
    if (!token || !expirationTime) {
      return null;
    }
    const currentTime = new Date().getTime();
    if (currentTime > parseInt(expirationTime)) {
      localStorage.removeItem('auth_token');
      localStorage.removeItem('auth_token_expiration');
      return null;
    }
    return token;
  }
  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('role');
  }
  isLoggedIn(): boolean {
    return localStorage.getItem('auth_token') !== null;
  }
  register(user: FormData): Observable<any> {
    return this.http.post(this.apiRegisterUrl, user)
  }

  setToken(token: string): void {
    const expirationTime = new Date().getTime() + 1000 * 60 * 60 * 24;
    localStorage.setItem('auth_token', token);
    localStorage.setItem('auth_token_expiration', expirationTime.toString());
  }

}

