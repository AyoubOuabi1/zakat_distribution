export interface User {
  id: number;
  fullName: string;
  email: string;
  address: string;
  phoneNumber: string;
  canton: string;
  postalCode: string;
  role: string;
  newPassword?: string;
  confirmNewPassword?: string;
  paymentMethod?: string;
  bankDetailsImage?: string;
}
