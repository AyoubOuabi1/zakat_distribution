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

  logout(): void {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }


  register(userData: any): Observable<any> {
    return this.http.post(this.apiRegisterUrl, userData);
  }
}
