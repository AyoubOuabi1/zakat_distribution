import { Component } from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  user = {
    email: '',
    password: ''
  };
  loginError: string = '';


  constructor(private authService: AuthService, private router: Router) {
  }

  onLogin() {
    this.authService.login(this.user.email, this.user.password).subscribe(
      (response) => {
        localStorage.setItem('auth_token', response.token);
        this.router.navigate(['']);
      },
      (error) => {
        this.loginError = 'Invalid credentials, please try again.';
      }
    );
  }

  togglePassword(inputId: string) {
    const input = document.getElementById(inputId) as HTMLInputElement;
    const eyeIcon = document.getElementById('eye-icon');

    if (input.type === 'password') {
      input.type = 'text';
      eyeIcon?.classList.remove('fa-eye');
      eyeIcon?.classList.add('fa-eye-slash');
    } else {
      input.type = 'password';
      eyeIcon?.classList.remove('fa-eye-slash');
      eyeIcon?.classList.add('fa-eye');
    }
  }
}
