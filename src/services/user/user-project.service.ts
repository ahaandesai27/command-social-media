import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../env/env";
import { User } from "../../models/User";
import { Project } from "../../models/Project";

@Injectable({
    providedIn: 'root'
})
export class UserProjectService {
    private readonly API = `${environment.apiBaseUrl}`

    constructor(private http: HttpClient) {}

    public addProject(userId: number, project: Project): Observable<Project> {
        return this.http.post<Project>(`${this.API}/users/${userId}/projects`, project)
    }   

    public updateProject(project: Project) {
        const projectId = project.id;
        return this.http.put<Project>(`${this.API}/projects/${projectId}`, project);
    }

    public deleteProject(projectId: number) {
        this.http.delete(`${this.API}/projects/${projectId}`);
    }

    
}