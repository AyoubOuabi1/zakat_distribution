import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "../services/auth/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService : AuthService) {}


  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    const isLoggedIn = this.authService.isLoggedIn();
    if(isLoggedIn){
      const authReq = request.clone({
        headers: request.headers.set('Authorization', `Bearer ${this.authService.getToken()}`)
      });
      return next.handle(authReq);
    }

    return next.handle(request);
  }
}
