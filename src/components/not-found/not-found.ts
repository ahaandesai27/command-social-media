import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-not-found',
  imports: [],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound implements OnInit {
  private currentPath: string = '';

  constructor(
    private router: Router,
    private location: Location
  ) {}

  ngOnInit() {
    this.currentPath = this.location.path() || '/';
  }

  getCurrentPath(): string {
    return this.currentPath;
  }

  goHome() {
    this.router.navigate(['']);
  }

  goToProfile() {
    this.router.navigate(['/profile']);
  }

  goBack() {
    this.location.back();
  }

  showHelp() {
    // Could open a help modal or navigate to help page
    alert('Available commands:\n\nNavigation:\n• go home - Return to homepage\n• show profile - View profile page\n\nPress Ctrl + K to open command palette');
  }
}
