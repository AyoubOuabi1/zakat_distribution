import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiLoginUrl = `http://localhost:8080/auth/login`;
  private apiRegisterUrl = `http://localhost:8080/auth/register`;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
    return this.http.post(this.apiLoginUrl, loginData);
  }
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }
  logout(): void {
    localStorage.removeItem('auth_token');
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('auth_token') !== null;
  }


  register(userData: any): Observable<any> {
    return this.http.post(this.apiRegisterUrl, userData);
  }
}
