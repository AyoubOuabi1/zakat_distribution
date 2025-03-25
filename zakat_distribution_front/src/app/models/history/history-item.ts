export interface HistoryItem {
  id: number;
  transactionType: 'ZAKAT_DISTRIBUTION' | 'DONATION_RECEIVED'; // More descriptive names
  fullName: string;
  amount: number;
  date: Date;
  paymentMethod: string;
  referenceNumber?: string; // Optional field for tracking
}
