import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user/user.service";
import { User } from "../../models/user/user";
import {environment} from "../../environment";

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
    newPassword: '',
    confirmNewPassword: ''
  };
  role: string | null | undefined;
  passwordMismatch: boolean = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUserProfile();
    this.role = this.userService.getUserRole();
  }

  fetchUserProfile(): void {
    this.userService.getUserProfile().subscribe(
      (data: User) => {
        this.user = data;
      },
      (error) => {
        console.error('Error fetching user profile', error);
      }
    );
  }

  checkPasswordMatch(): void {
    this.passwordMismatch = this.user.newPassword !== this.user.confirmNewPassword;
  }

  onSubmit(): void {
    if (this.user.newPassword && this.user.newPassword !== this.user.confirmNewPassword) {
      this.passwordMismatch = true;
      return;
    }
    const updateUser: User = { ...this.user };
    if (!updateUser.newPassword) {
      delete updateUser.newPassword;
      delete updateUser.confirmNewPassword;
    }
    this.userService.updateUserProfile(updateUser).subscribe(
      (data: User) => {
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
      (error) => {
        Swal.fire({
          title: 'Success!',
          text: 'Error updating your data',
          icon: 'error',
          confirmButtonText: 'OK'
        })
      }
    );
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.user.bankDetailsImage = file;
    }
  }

  getImageUrl(imageName: string): string {
    // Replace with your backend's base URL
    const baseUrl = `${environment.apiUrl}/uploads/bank-details/`;
    return `${baseUrl}${imageName}`;
  }
}
