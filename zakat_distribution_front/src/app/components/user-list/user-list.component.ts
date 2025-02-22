import { Component, OnInit } from '@angular/core';
import { User } from "../../models/user/user";
import { UserService } from "../../services/user/user.service";
import { environment } from "../../environment";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  selectedUser: User | null = null;
  selectedRole: string = 'ALL';
  searchTerm: string = '';

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
      this.applyFilters();
    });
  }

  getImageUrl(imageName: string | null): string {
    if (!imageName) return '';
    const baseUrl = `${environment.staticFileUrl}/uploads/bank-details/`;
    return `${baseUrl}${imageName}`;
  }

  applyFilters(): void {
    let filtered = this.users;

    if (this.selectedRole !== 'ALL') {
      filtered = filtered.filter(user => user.role === this.selectedRole);
    }
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase().trim();
      filtered = filtered.filter(user =>
        user.fullName.toLowerCase().includes(searchLower) ||
        user.email.toLowerCase().includes(searchLower) ||
        user.canton?.toLowerCase().includes(searchLower) ||
        user.phoneNumber?.toLowerCase().includes(searchLower)
      );
    }

    this.filteredUsers = filtered;
    this.totalUsers = this.filteredUsers.length;
    this.currentPage = 1; // Reset to first page when filtering
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm = input.value;
    this.applyFilters();
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

  deleteUser(userId: number): void {
    Swal.fire({
      title: "Do you want to delete this item?",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Dont't delete",
      denyButtonText: `delete`
    }).then((result: { isConfirmed: any; isDenied: any; }) => {
      if (result.isConfirmed) {
        Swal.fire("item are not deleted", "", "info");
      } else if (result.isDenied) {
        this.userService.deleteUserById(userId).subscribe({
          next: () => {
            Swal.fire("deleted!", "", "success");
            this.loadUsers();
          },
          error: (err) => {
             Swal.fire("Failed to delete user:",err, "error");
          }
        });


      }
    });
  }

  protected readonly Math = Math;
}
