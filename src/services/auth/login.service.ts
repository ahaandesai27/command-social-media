import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../env/env";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API = `${environment.apiBaseUrl}/auth`
    isAuthenticated = signal(false);

    constructor(private http: HttpClient) {}

    login(credentials: { username: string; password: string}): Observable<{token: string}> {
      return this.http.post<{token: string}>(
        `${this.API}/login`,
        credentials
      ).pipe(
        tap(res => {
          localStorage.setItem('token', res.token);
          this.isAuthenticated.set(true);
        })
      )
    }

    // pipe takes an observable (such as returned by http.post and passes its values through a sequence of operators)
    // tap is a side effect operator - allows us to do something without changing values

    logout() {
      localStorage.removeItem('token');
      this.isAuthenticated.set(false);
    }

    getToken() {
      return localStorage.getItem('token');
    }
}