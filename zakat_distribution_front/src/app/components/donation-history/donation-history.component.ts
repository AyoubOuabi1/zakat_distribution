import { Component, OnInit } from '@angular/core';
import { Donation } from '../../models/donation/donation';
import { DonationService } from '../../services/donation/donation.service';
import { PdfService } from '../../services/pdf.service';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-donation-history',
  templateUrl: './donation-history.component.html',
  styleUrls: ['./donation-history.component.css'],
})
export class DonationHistoryComponent implements OnInit {
  donations: Donation[] = [];
  filteredDonations: Donation[] = [];
  p: number = 1;
  currentPage: number = 1;
  pageSize: number = 10;
  totalDonations: number = 0;
  filters = {
    donorName: '',
    amount: null,
    id: null,
  };

  constructor(
    private donationService: DonationService,
    private pdfService: PdfService) {}

  ngOnInit(): void {
    this.loadDonations();
  }

  loadDonations(): void {
    this.donationService.getAllDonationsWithDonorFullName().subscribe(
      (data) => {
        this.donations = data;
        this.applyFilters();
      },
      (error) => {
        console.error('Error fetching donations:', error);
      }
    );
  }

  applyFilters(): void {
    this.filteredDonations = this.donations.filter((donation) => {
      const matchesDonorName = this.filters.donorName
        ? (donation.donorFullName || '').toLowerCase().includes(this.filters.donorName.toLowerCase())
        : true;

      const matchesAmount = this.filters.amount
        ? donation.amount === this.filters.amount
        : true;

      const matchesId = this.filters.id
        ? donation.id === this.filters.id
        : true;

      return matchesDonorName && matchesAmount && matchesId;
    });

    this.totalDonations = this.filteredDonations.length;
    this.currentPage = 1; // Reset to the first page after filtering
  }

  // Get paginated donations for the current page
  get paginatedDonations(): Donation[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredDonations.slice(startIndex, startIndex + this.pageSize);
  }

  // Calculate total pages
  get totalPages(): number {
    return Math.ceil(this.totalDonations / this.pageSize);
  }

  // Navigate to the next page
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  // Navigate to the previous page
  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  // Generate invoice for a donation
  generateInvoice(donation: Donation): void {
    this.pdfService.generateInvoice(donation);
  }
}
