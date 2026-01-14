import { Component, HostListener, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Profile } from "../components/profile/profile";
import { Command } from "../components/command/command";
import { BrandHeader } from "../components/brand-header/brand-header";
import { CommandToggleService } from '../services/command/toggle.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Command, BrandHeader],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(private command: CommandToggleService) {}

  protected readonly title = signal('code-socialmedia');

  // On pressing ctrl K , window toggles
  // On pressing esc, window closes
  @HostListener('window:keydown', ['$event'])
  handleKeydown(event: KeyboardEvent) {
    if ((event.ctrlKey || event.metaKey) && event.key == 'k') {
      event.preventDefault();
      this.command.toggle();
    }

    if (event.key == 'Escape') {
      this.command.close();
    }
  }
}
