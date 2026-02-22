import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../env/env";

@Injectable({
    providedIn: 'root'
})
export class RegisterSerivce {
    private readonly API = `${environment.apiBaseUrl}/auth`

    constructor(private http: HttpClient) {}

    register(credentials: {username: string, password: string}): Observable<void> {
      // Note: Angular expects JSON by default so parse error was happening
      return this.http.post<void>(
        `${this.API}/register`,
        credentials
      );
    }
}