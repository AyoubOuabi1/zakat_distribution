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
 import {UserListComponent} from "./components/user-list/user-list.component";
import {ReceiveHistoryComponent} from "./components/receive-history/receive-history.component";
import {DonationTableComponent} from "./components/my-donation/donation-table.component";
import {DonationHistoryComponent} from "./components/donation-history/donation-history.component";
import {FinancialHistoryComponent} from "./components/financial-history/financial-history.component";


const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'auth/login', component: SignInComponent },
  { path: 'auth/register', component: SignUpComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'profile', component: UserProfileComponent },
      { path: 'mydonation', component: DonationTableComponent },
      { path: 'all-history', component: FinancialHistoryComponent },
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
