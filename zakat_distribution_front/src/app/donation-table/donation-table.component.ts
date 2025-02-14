import { Component, AfterViewInit, OnInit } from '@angular/core';
import {DonationService} from "../services/donation/donation.service";

interface Donation {
  id?: number;
  amount: number;
  paymentMethod: string;
  paymentDetails: string;
  date: string;
}

declare var bootstrap: any;

@Component({
  selector: 'app-donation-table',
  templateUrl: './donation-table.component.html',
  styleUrls: ['./donation-table.component.css']
})
export class DonationTableComponent implements AfterViewInit, OnInit {
  donations: Donation[] = [];
  selectedDonation: Donation = { amount: 0, paymentMethod: '', paymentDetails: '', date: '' };
  isEditing: boolean = false;
  editingIndex: number | null = null;
  private modal: any;

  constructor(private donationService: DonationService) {}

  ngOnInit() {
    this.loadDonations();
  }

  ngAfterViewInit() {
    const modalElement = document.getElementById('donationModal');
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
    }
  }

  // Load donations from the backend
  loadDonations() {
    this.donationService.getDonations().subscribe((data) => {
      this.donations = data;
    });
  }

  openModalForAdd() {
    this.isEditing = false;
    this.selectedDonation = { amount: 0, paymentMethod: '', paymentDetails: '', date: '' };
    this.modal.show();
  }

  openModalForEdit(index: number) {
    this.isEditing = true;
    this.editingIndex = index;
    this.selectedDonation = { ...this.donations[index] };
    this.modal.show();
  }

  saveDonation() {
    if (this.isEditing && this.editingIndex !== null) {
      const id = this.donations[this.editingIndex].id!;
      this.donationService.updateDonation(id, this.selectedDonation).subscribe(() => {
        this.loadDonations();
      });
    } else {
      this.donationService.addDonation(this.selectedDonation).subscribe(() => {
        this.loadDonations();
      });
    }
    this.modal.hide();
  }

  deleteDonation(index: number) {
    const id = this.donations[index].id!;
    this.donationService.deleteDonation(id).subscribe(() => {
      this.loadDonations();
    });
  }
}
