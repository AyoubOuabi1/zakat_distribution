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
    bankDetailsImage: '',
    newPassword: '',
    confirmNewPassword: ''
  };
  role: string | null | undefined;
  passwordMismatch: boolean = false;
  selectedFile: File | null = null;

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

    if (this.user.paymentMethod == 'TWINT') {
      this.user.bankDetailsImage = null;
      this.selectedFile = null;
    }

    const formData = new FormData();
    formData.append('userDTO', new Blob([JSON.stringify(this.user)], { type: 'application/json' }));

    if (this.selectedFile) {
      formData.append('bankDetailsImage', this.selectedFile);
    }

    this.userService.updateUserProfile(formData).subscribe(
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
          title: 'Error!',
          text: 'Error updating your data',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  onImageChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  getImageUrl(imageName: string): string {
    const baseUrl = `${environment.apiUrl}/uploads/bank-details/`;
    return `${baseUrl}${imageName}`;
  }

  onRoleChange() {
    var paymentMethod=this.user.paymentMethod;
    console.log(this.user.paymentMethod)
    if (paymentMethod === 'TWINT') {
      this.user.bankDetailsImage = null;
      this.selectedFile = null;
    }
  }

}
