import { Component, OnInit } from '@angular/core';
 import { PdfService } from '../../services/pdf.service';
import { DatePipe } from '@angular/common';
import {HistoryItem} from "../../models/history/history-item";
import {HistoryService} from "../../services/history/history.service";

@Component({
  selector: 'app-financial-history',
  templateUrl: './financial-history.component.html',
  styleUrls: ['./financial-history.component.css'],
  providers: [DatePipe]
})
export class FinancialHistoryComponent implements OnInit {
  // Data properties
  allTransactions: HistoryItem[] = [];
  filteredTransactions: HistoryItem[] = [];
  displayedTransactions: HistoryItem[] = [];

  // Pagination
  currentPage = 1;
  pageSize = 10;
  totalItems = 0;

  // Filters
  filters = {
    searchTerm: '',
    transactionType: '',
    startDate: '',
    endDate: '',
    minAmount: null as number | null,
    maxAmount: null as number | null
  };

  // Summary calculations
  totalDebits = 0;
  totalCredits = 0;
  netBalance = 0;

  constructor(
    private historyService: HistoryService,
    private pdfService: PdfService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.loadFinancialHistory();
  }

  loadFinancialHistory(): void {
    this.historyService.getFinancialHistory().subscribe({
      next: (transactions) => {
        this.allTransactions = transactions;
        this.applyFilters();
      },
      error: (err) => console.error('Error loading financial history:', err)
    });
  }

  applyFilters(): void {
    // Apply all filters
    this.filteredTransactions = this.allTransactions.filter(transaction => {
      const matchesSearch = !this.filters.searchTerm ||
        transaction.fullName.toLowerCase().includes(this.filters.searchTerm.toLowerCase()) ||
        transaction.referenceNumber?.toLowerCase().includes(this.filters.searchTerm.toLowerCase());

      const matchesType = !this.filters.transactionType ||
        transaction.transactionType === this.filters.transactionType;

      const matchesDateRange = (
        (!this.filters.startDate || new Date(transaction.date) >= new Date(this.filters.startDate)) &&
        (!this.filters.endDate || new Date(transaction.date) <= new Date(this.filters.endDate)
        ));

      const matchesAmount = (
        (!this.filters.minAmount || transaction.amount >= this.filters.minAmount) &&
        (!this.filters.maxAmount || transaction.amount <= this.filters.maxAmount)
      );

      return matchesSearch && matchesType && matchesDateRange && matchesAmount;
    });

    // Calculate totals
    this.calculateTotals();

    // Update pagination
    this.totalItems = this.filteredTransactions.length;
    this.currentPage = 1;
    this.updateDisplayedTransactions();
  }

  calculateTotals(): void {
    this.totalDebits = this.filteredTransactions
      .filter(t => t.transactionType === 'DONATION_RECEIVED')
      .reduce((sum, t) => sum + t.amount, 0);

    this.totalCredits = this.filteredTransactions
      .filter(t => t.transactionType === 'ZAKAT_DISTRIBUTION')
      .reduce((sum, t) => sum + t.amount, 0);

    this.netBalance = this.totalDebits - this.totalCredits;
  }

  updateDisplayedTransactions(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    this.displayedTransactions = this.filteredTransactions.slice(
      startIndex,
      startIndex + this.pageSize
    );
  }

  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.updateDisplayedTransactions();
  }

  generateDocument(transaction: HistoryItem): void {
    const docType = transaction.transactionType === 'DONATION_RECEIVED' ? 'Receipt' : 'Voucher';
   // this.pdfService.generateDocument(transaction, docType);
  }

  exportToCSV(): void {
    const headers = [
      'Date', 'Type', 'Name', 'Payment Method', 'Reference',
      'Amount', 'Debit', 'Credit'
    ];

    const csvContent = [
      headers.join(','),
      ...this.filteredTransactions.map(t => [
        this.datePipe.transform(t.date, 'yyyy-MM-dd'),
        t.transactionType === 'DONATION_RECEIVED' ? 'Donation' : 'Zakat',
        `"${t.fullName}"`, // Wrap in quotes to handle commas in names
        t.paymentMethod,
        t.referenceNumber || '',
        t.amount.toFixed(2),
        t.transactionType === 'DONATION_RECEIVED' ? t.amount.toFixed(2) : '',
        t.transactionType === 'ZAKAT_DISTRIBUTION' ? t.amount.toFixed(2) : ''
      ].join(','))
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', `financial_history_${new Date().toISOString().slice(0,10)}.csv`);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  resetFilters(): void {
    this.filters = {
      searchTerm: '',
      transactionType: '',
      startDate: '',
      endDate: '',
      minAmount: null,
      maxAmount: null
    };
    this.applyFilters();
  }

  protected readonly Math = Math;
}
