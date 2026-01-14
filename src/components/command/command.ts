import {
  Component,
  signal,
  ViewChild,
  ElementRef,
  effect,
  HostListener
} from '@angular/core';
import { Router } from '@angular/router';
import { CommandToggleService } from '../../services/command/toggle.service';
import { CommandProcessService } from '../../services/command/process.service';
import { AuthService } from '../../services/auth/login.service';
import { Post } from '../../models/Post';

@Component({
  selector: 'app-command',
  imports: [],
  templateUrl: './command.html',
  styleUrl: './command.css',
})
export class Command {

  @ViewChild('commandInput')
  private commandInput!: ElementRef<HTMLInputElement>;

  private readonly _input = signal('');
  private readonly _logs = signal('');
  private readonly _history = signal<string[]>([]);

  constructor(
    public commandProcess: CommandProcessService,
    public commandToggle: CommandToggleService,
    public authService: AuthService,
    public router: Router
  ) {
    effect(() => {
      if (this.commandToggle.isOpen()) {
        setTimeout(() => {
          this.commandInput?.nativeElement?.focus();
        }, 100);
      }
    });
  }

  get input() {
    return this._input();
  }

  get logs() {
    return this._logs();
  }

  setLogs(value: string) {
    this._logs.set(value);
  }

  appendLogs(value: string) {
    this._logs.update(current => current + (current ? '\n' : '') + value);
  }

  clearLogs() {
    this._logs.set('');
  }

  onInput(value: string) {
    this._input.set(value);
  }

  onEnter() {
    const command = this.input.trim();
    if (command == "clear") {
      this.setLogs("");
      return;
    }

    this._input.set(command);
    this._history.update(h => [...h, command]);

    const result = this.commandProcess.processCommand(command);

    switch (result.type) {
      case 'navigation':
        this.router.navigateByUrl(result.path);
        this.commandToggle.close();
        break;

      case 'log':
        this.appendLogs(result.content);
        break;

      case 'action':
        if (result.name === 'logout') {
          this.setLogs("Logout successful!");
          this.router.navigateByUrl('/login');
          break;
        }

        if (result.action$) {
          result.action$.subscribe({
            next: (data) => {
              if (result.name === 'post') {
                this.setLogs("Post created!");
              }

              else if (result.name === 'login') {
                this.setLogs("Login successful!");
              }

              else if (result.name === 'editProfile') {
                this.setLogs("Profile updated!");
              }

              else if (result.name == 'followUser') {
                this.setLogs('User followed successfully!');
              }

              else if (result.name == 'unfollowUser') {
                this.setLogs('User unfollowed successfully!');
              }

              else if (result.name == 'searchUsers') {
                data = data as string[];
                if (data.length > 0) {
                  this.setLogs(`Found ${data.length} user(s):\n${data.join('\n')}`);
                } else {
                  this.setLogs('No users found.');
                }
              }

              else if (result.name == 'searchPosts') {
                data = data as Post[];
                if (data.length > 0) {
                  const postTitles = data.map((post: Post) => `${post.title} (by ${post.username})`);
                  this.setLogs(`Found ${data.length} post(s):\n${postTitles.join('\n')}`);
                } else {
                  this.setLogs('No posts found.');
                }
              }
            },
            error: (err) => {
              this.appendLogs(
                err?.error?.message
                  ? `Error: ${err.error.message}`
                  : 'Action failed'
              );
            }
          });
        }

        break;

      case 'error':
        this.appendLogs(`Error: ${result.message || 'Unknown error'}`);
        break;
    }

    this._input.set('');
    this.commandInput.nativeElement.value = '';
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if (event.key !== 'ArrowUp') return;

    const history = this._history();
    if (!history.length) return;

    event.preventDefault();

    const last = history[history.length - 1];
    this._history.set(history.slice(0, -1));

    this._input.set(last);
    this.commandInput.nativeElement.value = last;
  }

}
