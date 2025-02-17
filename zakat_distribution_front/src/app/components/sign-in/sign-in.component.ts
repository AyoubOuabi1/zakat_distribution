import { Component } from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
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
        localStorage.setItem('role', response.role);
        this.router.navigate(['']);
      },
      (error) => {
        Swal.fire({
          title: 'Error!',
          text: 'Invalid credentials, please try again.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }
}
