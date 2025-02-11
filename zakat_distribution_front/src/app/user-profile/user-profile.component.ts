import { Component, OnInit } from '@angular/core';
import {UserService} from "../services/user/user.service";
import {User} from "../models/user/user";

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
    role: ''
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  // Fetch the user's profile data
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

  // Handle form submission
  onSubmit(): void {
    this.userService.updateUserProfile(this.user).subscribe(
      (data: User) => {
        console.log('Profile updated successfully', data);
      },
      (error) => {
        console.error('Error updating user profile', error);
        // Optionally, show an error message to the user
      }
    );
  }
}
