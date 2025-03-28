import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Donation} from "../../models/donation/donation";
import {environment} from "../../environment";

@Injectable({
  providedIn: 'root'
})
export class DonationService {
  private apiUrl = `${environment.apiUrl}/donation`;

  constructor(private http: HttpClient) {}

  getDonations(): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.apiUrl}/history`);
  }

  addDonation(donation: Donation): Observable<Donation> {
    const payload = {
      amount: donation.amount,
      paymentMethod: donation.paymentMethod,
      paymentDetails: donation.paymentDetails,
      date: donation.date
    };
    return this.http.post<Donation>(this.apiUrl, payload);
  }

  updateDonation(id: number, donation: Donation): Observable<Donation> {
    return this.http.put<Donation>(`${this.apiUrl}/${id}`, donation);
  }

  deleteDonation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAllDonationsWithDonorFullName(): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.apiUrl}/all`);
  }
}
