import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user/user.service";
import { User } from "../../models/user/user";
import { environment } from "../../environment";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: User = {
    id: 0,
    fullName: '',
    email: '',
    address: '',
    phoneNumber: '',
    canton: '',
    postalCode: '',
    role: '',
    paymentMethod: '',
    bankDetailsImage: null,
    newPassword: '',
    confirmNewPassword: '',
    totalReceived: 0,
    totalDonated: 0
  };
  role: string | null = null;
  passwordMismatch: boolean = false;
  selectedFile: File | null = null;
  validationErrors: { [key: string]: string } = {};

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUserProfile();
    this.role = this.userService.getUserRole();
  }

  fetchUserProfile(): void {
    this.userService.getUserProfile().subscribe({
      next: (data: User) => {
        this.user = data;
      },
      error: (error) => {
        console.error('Error fetching user profile', error);
      }
    });
  }

  checkPasswordMatch(): void {
    this.passwordMismatch = this.user.newPassword !== this.user.confirmNewPassword;
  }

  onSubmit(): void {
    // Check if passwords match
    if (this.user.newPassword && this.user.newPassword !== this.user.confirmNewPassword) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;

    const formData = new FormData();
    formData.append('userDTO', new Blob([JSON.stringify(this.user)], { type: 'application/json' }));

    if (this.selectedFile) {
      formData.append('bankDetailsImage', this.selectedFile);
    }

    // Call the update profile service
    this.userService.updateUserProfile(formData).subscribe({
      next: (data: User) => {
        Swal.fire({
          title: 'Success!',
          text: 'Your data has been updated successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.user.newPassword = '';
          this.user.confirmNewPassword = '';
        });
      },
      error: (error) => {
        if (error.error && error.error.details) {
          this.validationErrors = error.error.details;
        } else {
          Swal.fire({
            title: 'Error!',
            text: error.error?.error || 'Error updating your data',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      }
    });
  }

  onImageChange(event: any): void {
    if (event.target.files && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  getImageUrl(imageName: string | null): string {
    if (!imageName) return '';
    const baseUrl = `${environment.staticFileUrl}/uploads/bank-details/`;
    return `${baseUrl}${imageName}`;
  }

  onPaymentMethodChange(): void {
    if (this.user.paymentMethod === 'TWINT') {
      this.clearBankDetails();
    }
  }

  onRoleChange(): void {
    if (this.user.role === 'DONOR') {
      this.clearBankDetails();
      this.user.paymentMethod = null;
    }
  }

  private clearBankDetails(): void {
    this.user.bankDetailsImage = null;
    this.selectedFile = null;
  }

  hasError(field: string): boolean {
    return !!this.validationErrors[field];
  }

  getError(field: string): string {
    return this.validationErrors[field] || '';
  }
}
