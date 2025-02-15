import { Component } from '@angular/core';
import {UserService} from "../services/user/user.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  isSidebarOpen = true;
  role?: string | null;
  constructor(private userService: UserService) {
    this.role=userService.getUserRole()
  }
  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
}
