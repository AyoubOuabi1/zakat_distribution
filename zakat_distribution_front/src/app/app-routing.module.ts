import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AppComponent} from "./app.component";
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {HomeComponent} from "./home/home.component";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";
import {AuthGuard} from "./guards/auth.guard";
import {AuthInterceptor} from "./interceptors/auth.interceptor";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {DonationTableComponent} from "./components/donation-table/donation-table.component";
import {UserListComponent} from "./components/user-list/user-list.component";
import {ReceiveHistoryComponent} from "./components/receive-history/receive-history.component";


const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'auth/login', component: SignInComponent },
  { path: 'auth/register', component: SignUpComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'profile', component: UserProfileComponent },
      { path: 'donation/history', component: DonationTableComponent },
      { path: 'users', component: UserListComponent },
      { path: 'receive/history', component: ReceiveHistoryComponent },
      { path: '', redirectTo: 'profile', pathMatch: 'full' }
    ], canActivate: [AuthGuard]
  },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
