import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Receive} from "../../models/receive/receive";

@Injectable({
  providedIn: 'root'
})
export class ReceiveHistoryService {
  private apiUrl = 'http://localhost:8080/api/zakat';

  constructor(private http: HttpClient) {}

  getReceiveHistory(): Observable<Receive[]> {
    return this.http.get<Receive[]>(`${this.apiUrl}/history`);
  }

  addReceiveHistory(history: Receive): Observable<Receive> {
    return this.http.post<Receive>(this.apiUrl, history);
  }

  updateReceiveHistory(id: number, history: Receive): Observable<Receive> {
    return this.http.put<Receive>(`${this.apiUrl}/${id}`, history);
  }

  deleteReceiveHistory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
