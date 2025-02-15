import { Component, OnInit } from '@angular/core';
import {User} from "../models/user/user";
import {UserService} from "../services/user/user.service";
@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  selectedUser: User | null = null;
  selectedRole: string = 'ALL'; // Default filter

  // Pagination
  currentPage: number = 1;
  pageSize: number = 10;
  totalUsers: number = 0;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe((data: User[]) => {
      this.users = data.filter(user => user.role !== 'ADMIN');
      this.applyFilter();
    });
  }

  applyFilter(): void {
    if (this.selectedRole === 'ALL') {
      this.filteredUsers = this.users;
    } else {
      this.filteredUsers = this.users.filter(user => user.role === this.selectedRole);
    }
    this.totalUsers = this.filteredUsers.length;
    this.currentPage = 1; // Reset to first page when filtering
  }

  get paginatedUsers(): User[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredUsers.slice(startIndex, startIndex + this.pageSize);
  }

  nextPage(): void {
    if ((this.currentPage * this.pageSize) < this.totalUsers) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  showUserDetails(user: User): void {
    this.selectedUser = user;
  }

  closeModal(): void {
    this.selectedUser = null;
  }

  protected readonly Math = Math;
}
