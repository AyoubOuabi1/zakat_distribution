import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Receive} from "../../models/receive/receive";
import {environment} from "../../environment";

@Injectable({
  providedIn: 'root'
})
export class ReceiveHistoryService {
  private apiUrl = `${environment.apiUrl}/zakat`;

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
