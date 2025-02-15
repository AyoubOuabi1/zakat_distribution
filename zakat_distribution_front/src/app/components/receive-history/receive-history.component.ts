import { Component, AfterViewInit, OnInit } from '@angular/core';
import { Receive } from '../../models/receive/receive';
import { ReceiveHistoryService } from '../../services/receive/receive.service';

declare var bootstrap: any;

@Component({
  selector: 'app-receive-history',
  templateUrl: './receive-history.component.html',
  styleUrls: ['./receive-history.component.css']
})
export class ReceiveHistoryComponent implements AfterViewInit, OnInit {
  receiveHistory: Receive[] = [];
  selectedHistory: Receive = { id: 0, amountReceived: 0, dateReceived: '' };
  isEditing: boolean = false;
  editingIndex: number | null = null;
  private modal: any;

  constructor(private receiveHistoryService: ReceiveHistoryService) {}

  ngOnInit(): void {
    this.fetchReceiveHistory();
  }

  ngAfterViewInit(): void {
    const modalElement = document.getElementById('receiveHistoryModal');
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
    }
  }

  fetchReceiveHistory(): void {
    this.receiveHistoryService.getReceiveHistory().subscribe(data => {
      this.receiveHistory = data;
    });
  }

  openModalForAdd(): void {
    this.isEditing = false;
    this.selectedHistory = { id: 0, amountReceived: 0, dateReceived: '' };
    this.modal.show();
  }

  openModalForEdit(index: number): void {
    this.isEditing = true;
    this.editingIndex = index;
    this.selectedHistory = { ...this.receiveHistory[index] };
    this.modal.show();
  }

  saveReceiveHistory(): void {
    if (this.isEditing && this.editingIndex !== null) {
      const id = this.receiveHistory[this.editingIndex].id!;
      this.receiveHistoryService.updateReceiveHistory(id, this.selectedHistory).subscribe(() => {
        this.fetchReceiveHistory();
        Swal.fire({
          title: 'Success!',
          text: 'Your operation has been updated successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.modal.hide();
        });
      });
    } else {
      this.receiveHistoryService.addReceiveHistory(this.selectedHistory).subscribe(() => {
        this.fetchReceiveHistory();
        Swal.fire({
          title: 'Success!',
          text: 'Your operation has been added successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.modal.hide();
        });

      });
    }
  }

  deleteReceiveHistory(id: number): void {
    Swal.fire({
      title: "Do you want to delete this item?",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Dont't delete",
      denyButtonText: `delete`
    }).then((result: { isConfirmed: any; isDenied: any; }) => {
      if (result.isConfirmed) {
        Swal.fire("item are not deleted", "", "info");
      } else if (result.isDenied) {
        this.receiveHistoryService.deleteReceiveHistory(id).subscribe(() => {
          this.receiveHistory = this.receiveHistory.filter(history => history.id !== id);
          Swal.fire("deleted!", "", "success");
        })
      }
    });

  }

}
