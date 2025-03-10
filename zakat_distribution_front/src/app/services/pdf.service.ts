import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import {Donation} from "../models/donation/donation";

@Injectable({
  providedIn: 'root', // Make the service available globally
})
export class PdfService {
  constructor() {}

  generateInvoice(donation: Donation) {
    const doc = new jsPDF();

    doc.setDrawColor(0, 102, 0);
    doc.setLineWidth(1);
    doc.rect(10, 10, 190, 277);

    const img = new Image();
    doc.addImage("../../../assets/images/zakat.png", 'PNG', 70, 20, 60, 25);

    // Add a title with styling
    doc.setFontSize(24);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 102, 0); // Green color
    doc.text('Zakat Donation Invoice', 105, 53, { align: 'center' });

    doc.setLineWidth(0.5);
    doc.line(50, 55, 160, 55);

    // Add a quote about Zakat
    doc.setFontSize(12);
    doc.setFont('times', 'italic');
    doc.setTextColor(0, 0, 0);
    doc.text('"The parable of those who spend their wealth in the way of Allah is that of a grain', 20, 70, { align: 'left' });
    doc.text('of corn: it grows seven ears, and each ear has a hundred grains."', 20, 75, { align: 'left' });
    doc.text('- Quran (2:261)', 20, 80, { align: 'left' });

    // Add donor name below the Quranic verse
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 0, 0); // Black color
    doc.text(`Donor Name: ${donation.donorFullName}`, 20, 100);

    // Add donation details
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 0, 0);
    doc.text('Donation Details', 20, 120);

    doc.setFontSize(12);
    doc.setFont('helvetica', 'normal');
    doc.text(`Donation ID: ${donation.id}`, 20, 130);
    doc.text(`Amount: ${donation.amount}$`, 20, 140);
    doc.text(`Payment Method: ${donation.paymentMethod}`, 20, 150);
    doc.text(`Payment Details: ${donation.paymentDetails}`, 20, 160);
    doc.text(`Date: ${donation.date}`, 20, 170);

    // Add a thank you message
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 102, 0); // Green color
    doc.text('Thank You for Your Generosity!', 105, 190, { align: 'center' });

    // Add a decorative footer
    const generatedDate = new Date().toLocaleDateString(); // Get current date
    doc.setFontSize(10);
    doc.setFont('times', 'italic');
    doc.setTextColor(128, 128, 128); // Gray color
    doc.text(`Generated by Aktion.today - Generated Date: ${generatedDate}`, 105, 280, { align: 'center' });

    doc.save(`zakat_invoice_${donation.id}_${generatedDate}_${donation.donorFullName}.pdf`);
  }
}
