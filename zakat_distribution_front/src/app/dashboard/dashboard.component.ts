import { Component } from '@angular/core';
import {UserService} from "../services/user/user.service";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  isSidebarOpen = true;
  role?: string | null;
  currentLang: string;
  constructor(private userService: UserService,
              private authService: AuthService,
              private router: Router,
              private translate: TranslateService) {
    this.role=userService.getUserRole()
    this.currentLang = translate.currentLang || 'fr';
  }
  ngOnInit(): void {
    this.translate.setDefaultLang('fr');
  }
  switchLanguage(lang: string): void {
    this.currentLang = lang;
    this.translate.use(lang);
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  logOut() {
    this.authService.logout()
    this.router.navigate(['/auth/login']);
  }
}
