import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../env/env";
import { User } from "../../models/User";


@Injectable({
    providedIn: 'root'
})
export class UserService {
    private readonly API = `${environment.apiBaseUrl}/users`

    constructor(private http: HttpClient) {}

    getUserById(userId: number): Observable<User> {
      return this.http.get<User>(
        `${this.API}/${userId}`
      );
    }

    getUserByUsername(username: string): Observable<User> {
      return this.http.get<User>(
        `${this.API}/username/${username}`
      )
    }

    updateUser(userId: number, user: Partial<User>): Observable<User> {
      // updates everything except projects   
      return this.http.patch<User>(`${this.API}/${userId}`, user);
    }

    saveProjectByIndex(userId: number, projectIndex: number, project: any): Observable<any> {
      console.log(projectIndex)
      return this.http.post(`${this.API}/${userId}/projects/${projectIndex}`, project);
    }
}