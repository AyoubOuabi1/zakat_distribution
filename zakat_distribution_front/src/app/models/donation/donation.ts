export interface Donation {
  id?: number;
  amount: number;
  paymentMethod: string;
  paymentDetails: string;
  date: string;
  donorFullName?:string;
}

