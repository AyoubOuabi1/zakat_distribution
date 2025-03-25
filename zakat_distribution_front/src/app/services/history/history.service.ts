// src/app/services/history.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {HistoryItem} from "../../models/history/history-item";
import {environment} from "../../environment";

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  private apiUrl = `${environment.apiUrl}/history`;

  constructor(private http: HttpClient) { }

  getFinancialHistory(): Observable<HistoryItem[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(items => items.map(item => ({
          id: item.id,
          transactionType: item.type === 'ZAKAT' ? 'ZAKAT_DISTRIBUTION' : 'DONATION_RECEIVED',
          fullName: item.fullName,
          amount: item.amount,
          date: new Date(item.date),
          paymentMethod: item.paymentMethod
        }))
      ));
  }

  // Optional: Get history with pagination
  getPaginatedHistory(page: number, size: number): Observable<{items: HistoryItem[], total: number}> {
    const params = {
      page: page.toString(),
      size: size.toString()
    };
    return this.http.get<{items: HistoryItem[], total: number}>(`${this.apiUrl}/paginated`, { params });
  }

  // Optional: Filter history on server side
  getFilteredHistory(filters: any): Observable<HistoryItem[]> {
    return this.http.post<HistoryItem[]>(`${this.apiUrl}/filter`, filters);
  }
}
