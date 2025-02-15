import { Component } from '@angular/core';
import { AuthService } from "../../services/auth/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  user = {
    fullName: '',
    email: '',
    address: '',
    phoneNumber: '',
    canton: '',
    postalCode: '',
    role: 'DONOR',
    password: '',
    confirmPassword: ''
  };
  registerError: string = '';
  passwordMismatch: boolean = false;

  constructor(private registerService: AuthService, private router: Router) {}

  onRegister() {
    if (this.user.password !== this.user.confirmPassword) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;

    this.registerService.register(this.user).subscribe(
      (response) => {
        Swal.fire({
          title: 'Success!',
          text: 'You have successfully signed up.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['auth/login']);
        });
      },
      (error) => {
        this.registerError = 'Registration failed. Please try again later.';
        Swal.fire({
          title: 'Error!',
          text: this.registerError,
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  onSubmit() {
    this.onRegister();
  }

  checkPasswordMatch() {
    this.passwordMismatch = this.user.password !== this.user.confirmPassword;
  }
}
