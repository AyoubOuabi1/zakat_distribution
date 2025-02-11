import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../../models/user/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = `http://localhost:8080/user`;

  constructor(private http: HttpClient) {}

  getUserData():Observable<User> {
    return this.http.get<User>(this.userUrl)
  }
}
