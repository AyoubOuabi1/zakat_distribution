import { Component } from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  isRegistering: boolean = true;
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
  registerError: string = ''; // For registration errors

  constructor(private registerService: AuthService, private router: Router) {}

  onRegister() {
    if (this.user.password !== this.user.confirmPassword) {
      this.registerError = 'Passwords do not match!';
      return;
    }

    this.registerService.register(this.user).subscribe(
      (response) => {
        console.log(response);
        this.router.navigate(['/login']);
      },
      (error) => {
        this.registerError = 'Registration failed. Please try again later.';
      }
    );
  }

  togglePassword(inputId: string) {
    const input = document.getElementById(inputId) as HTMLInputElement;
    const eyeIcon = document.getElementById("eye-icon");

    if (input.type === "password") {
      input.type = "text";
      eyeIcon?.classList.remove("fa-eye");
      eyeIcon?.classList.add("fa-eye-slash");
    } else {
      input.type = "password";
      eyeIcon?.classList.remove("fa-eye-slash");
      eyeIcon?.classList.add("fa-eye");
    }
  }

  toggleForm() {
    this.isRegistering = !this.isRegistering;
  }

  onSubmit() {
    this.onRegister();
  }
}
