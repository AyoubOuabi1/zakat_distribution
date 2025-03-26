import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import { Donation } from "../models/donation/donation";
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root',
})
export class PdfService {
  constructor(private translate: TranslateService) {}

  async generateInvoice(donation: Donation) {
    // Load translations first
    const translations = await this.translate.get([
      'PDF.INVOICE_TITLE',
      'PDF.QURAN_VERSE_1',
      'PDF.QURAN_VERSE_2',
      'PDF.QURAN_VERSE_3',
      'PDF.DONOR_NAME',
      'PDF.DONATION_DETAILS',
      'PDF.DONATION_ID',
      'PDF.AMOUNT',
      'PDF.PAYMENT_METHOD',
      'PDF.PAYMENT_DETAILS',
      'PDF.DATE',
      'PDF.THANK_YOU',
      'PDF.FOOTER_TEXT'
    ]).toPromise();

    const doc = new jsPDF();

    doc.setDrawColor(0, 102, 0);
    doc.setLineWidth(1);
    doc.rect(10, 10, 190, 277);

    const img = new Image();
    doc.addImage("../../../assets/images/zakat.png", 'PNG', 70, 20, 60, 25);

    // Add title
    doc.setFontSize(24);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 102, 0);
    doc.text(translations['PDF.INVOICE_TITLE'], 105, 53, { align: 'center' });

    doc.setLineWidth(0.5);
    doc.line(50, 55, 160, 55);

    // Add Quran verse
    doc.setFontSize(12);
    doc.setFont('times', 'italic');
    doc.setTextColor(0, 0, 0);
    doc.text(translations['PDF.QURAN_VERSE_1'], 20, 70, { align: 'left' });
    doc.text(translations['PDF.QURAN_VERSE_2'], 20, 75, { align: 'left' });
    doc.text(translations['PDF.QURAN_VERSE_3'], 20, 80, { align: 'left' });

    // Add donor name
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 0, 0);
    doc.text(`${translations['PDF.DONOR_NAME']}: ${donation.donorFullName}`, 20, 100);

    // Add donation details
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 0, 0);
    doc.text(translations['PDF.DONATION_DETAILS'], 20, 120);

    doc.setFontSize(12);
    doc.setFont('helvetica', 'normal');
    doc.text(`${translations['PDF.DONATION_ID']}: ${donation.id}`, 20, 130);
    doc.text(`${translations['PDF.AMOUNT']}: ${donation.amount} CHF`, 20, 140);
    doc.text(`${translations['PDF.PAYMENT_METHOD']}: ${this.translate.instant('PAYMENT_METHOD.' + donation.paymentMethod)}`, 20, 150);
    doc.text(`${translations['PDF.PAYMENT_DETAILS']}: ${donation.paymentDetails}`, 20, 160);
    doc.text(`${translations['PDF.DATE']}: ${donation.date}`, 20, 170);

    // Thank you message
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0, 102, 0);
    doc.text(translations['PDF.THANK_YOU'], 105, 190, { align: 'center' });

    // Footer
    const generatedDate = new Date().toLocaleDateString();
    doc.setFontSize(10);
    doc.setFont('times', 'italic');
    doc.setTextColor(128, 128, 128);
    doc.text(`${translations['PDF.FOOTER_TEXT']} ${generatedDate}`, 105, 280, { align: 'center' });

    doc.save(`zakat_invoice_${donation.id}_${generatedDate}_${donation.donorFullName}.pdf`);
  }
}
