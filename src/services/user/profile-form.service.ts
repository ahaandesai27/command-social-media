import { Injectable } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { User } from '../../models/User';
import { Project } from '../../models/Project';

type ProfileFormValue = {
    username: string;
    name: string;
    title: string;
    about: string;
    location: string;
    avatar: string;
    techStack: string[];
    projects: {
        id: number;
        name: string;
        description: string;
        githubUrl: string;
    }[];
};


@Injectable({ providedIn: 'root' })
export class ProfileFormService {
    readonly form;

    constructor(private fb: FormBuilder) {
        // Initialises form 
        this.form = this.fb.nonNullable.group({
            username: ['', [Validators.required, Validators.minLength(3)]],
            name: ['', Validators.required],
            title: [''],
            about: [''],
            location: [''],
            avatar: [''],
            techStack: this.fb.array<string>([]),
            projects: this.fb.array<FormGroup>([])
        });
    }


    get techStack() {
        return this.form.controls.techStack;
    }

    get projects() {
        return this.form.controls.projects;
    }

    setUser(user: User) {
        // adds single values directly, while uses a function to add array values
        this.form.patchValue(user);

        this.setArray(this.techStack, user.techStack, tech =>
            this.fb.control(tech, Validators.required)
        );

        this.setArray(this.projects, user.projects, project =>
            this.projectGroup(project)
        );
    }

    addTech() {
        this.techStack.push(this.fb.control('', Validators.required));
    }

    removeTech(i: number) {
        this.techStack.removeAt(i);
    }

    addProject() {
        if (this.projects.length < 3) {
            this.projects.push(this.projectGroup());
        }
    }

    removeProject(i: number) {
        this.projects.removeAt(i);
    }

    getValue(): Partial<User> {
        const value = this.form.getRawValue() as ProfileFormValue;

        return {
            username: value.username,
            name: value.name,
            title: value.title,
            about: value.about,
            location: value.location,
            avatar: value.avatar,
            techStack: value.techStack,
            projects: value.projects
        };
    }


    private projectGroup(project?: Project) {
        // initialises nested form for project group, with validators
        return this.fb.group({
            id: [project?.id],
            name: [project?.name ?? '', Validators.required],
            description: [project?.description ?? '', Validators.required],
            githubUrl: [
                project?.githubUrl ?? '',
                [Validators.required, Validators.pattern(/^https?:\/\/.+/)]
            ]
        });
    }

    private setArray<T>(
        // Function to add array values 
        // takes in Form array, existing values 
        // factory is the function that converts each individual type into the form type 
        array: FormArray,
        values: T[] | undefined,
        factory: (v: T) => any
        ) {
            array.clear();
            values?.forEach(v => array.push(factory(v)));
    }
}
