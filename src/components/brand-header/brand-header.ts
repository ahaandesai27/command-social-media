import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CommandToggleService } from '../../services/command/toggle.service';

@Component({
  selector: 'app-brand-header',
  imports: [CommonModule],
  templateUrl: './brand-header.html',
  styleUrl: './brand-header.css',
})
export class BrandHeader {
  public showSidebar: boolean = false;

  constructor(
    private router: Router,
    private commandToggle: CommandToggleService
  ) {}

  onToggleSidebar() {
    this.showSidebar = !this.showSidebar;
  }

  onCloseSidebar() {
    this.showSidebar = false;
  }

  onToggleCommand() {
    this.commandToggle.toggle();
  }

  onNavigate(path: string) {
    this.router.navigate([path]);
    this.onCloseSidebar();
  }

  onBackdropClick(event: Event) {
    if (event.target === event.currentTarget) {
      this.onCloseSidebar();
    }
  }

}