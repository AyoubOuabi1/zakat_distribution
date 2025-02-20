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
    paymentMethod: 'TWINT',
    bankTransferImage: null,
    password: '',
    confirmPassword: ''
  };
  registerError: string = '';
  passwordMismatch: boolean = false;
  validationErrors: { [key: string]: string } = {}; // To store backend validation errors

  constructor(private registerService: AuthService, private router: Router) {}

  onRegister() {
    // Check if passwords match
    if (this.user.password !== this.user.confirmPassword) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;

    // Create FormData for the request
    const formData = new FormData();
    formData.append('fullName', this.user.fullName);
    formData.append('email', this.user.email);
    formData.append('address', this.user.address);
    formData.append('phoneNumber', this.user.phoneNumber);
    formData.append('canton', this.user.canton);
    formData.append('postalCode', this.user.postalCode);
    formData.append('role', this.user.role);
    formData.append('paymentMethod', this.user.paymentMethod);
    formData.append('password', this.user.password);
    formData.append('confirmPassword', this.user.confirmPassword);

    if (this.user.bankTransferImage) {
      formData.append('bankDetailsImage', this.user.bankTransferImage);
    }

    // Call the registration service
    this.registerService.register(formData).subscribe(
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
        if (error.error && error.error.details) {
          // Backend validation errors
          this.validationErrors = error.error.details;
        } else {
          // Generic error message
          this.registerError = error.error?.error || 'Registration failed. Please try again later.';
          Swal.fire({
            title: 'Error!',
            text: this.registerError,
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      }
    );
  }

  onSubmit() {
    this.onRegister();
  }

  checkPasswordMatch() {
    this.passwordMismatch = this.user.password !== this.user.confirmPassword;
  }

  onRoleChange() {
    this.user.paymentMethod = 'TWINT';
    this.user.bankTransferImage = null;
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.user.bankTransferImage = file;
    }
  }

  hasError(field: string): boolean {
    return !!this.validationErrors[field];
  }

  getError(field: string): string {
    return this.validationErrors[field] || '';
  }
}
