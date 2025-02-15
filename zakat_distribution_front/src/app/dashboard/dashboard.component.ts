import { Component } from '@angular/core';
import {UserService} from "../services/user/user.service";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  isSidebarOpen = true;
  role?: string | null;
  constructor(private userService: UserService,
              private authService: AuthService,
              private router: Router,) {
    this.role=userService.getUserRole()
  }
  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  logOut() {
    this.authService.logout()
    this.router.navigate(['/auth/login']);
  }
}
