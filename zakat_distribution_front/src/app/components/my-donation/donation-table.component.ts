import { Component, AfterViewInit, OnInit } from '@angular/core';
import { DonationService } from '../../services/donation/donation.service';
import { jsPDF } from 'jspdf';
import {Donation} from "../../models/donation/donation";
import {PdfService} from "../../services/pdf.service";



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

  constructor(private donationService: DonationService,
              private pdfService: PdfService,) {}

  ngOnInit() {
    this.loadDonations();
  }

  ngAfterViewInit() {
    const modalElement = document.getElementById('donationModal');
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
    }
  }

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
        Swal.fire({
          title: 'Success!',
          text: 'Your operation has been updated successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.modal.hide();
        });
        this.loadDonations();
      });
    } else {
      this.donationService.addDonation(this.selectedDonation).subscribe(() => {
        Swal.fire({
          title: 'Success!',
          text: 'Your operation has been added successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.modal.hide();
        });
        this.loadDonations();
      });
    }
  }

  deleteDonation(index: number) {
    const id = this.donations[index].id!;
    Swal.fire({
      title: "Do you want to delete this item?",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Don't delete",
      denyButtonText: `Delete`
    }).then((result: { isConfirmed: any; isDenied: any; }) => {
      if (result.isConfirmed) {
        Swal.fire("Item was not deleted", "", "info");
      } else if (result.isDenied) {
        this.donationService.deleteDonation(id).subscribe(() => {
          this.loadDonations();
          Swal.fire("Deleted!", "", "success");
        });
      }
    });
  }

  generateInvoice(donation: Donation) {
    this.pdfService.generateInvoice(donation);
  }
}
