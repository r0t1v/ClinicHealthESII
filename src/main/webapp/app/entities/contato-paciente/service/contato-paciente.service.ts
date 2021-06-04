import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContatoPaciente, getContatoPacienteIdentifier } from '../contato-paciente.model';

export type EntityResponseType = HttpResponse<IContatoPaciente>;
export type EntityArrayResponseType = HttpResponse<IContatoPaciente[]>;

@Injectable({ providedIn: 'root' })
export class ContatoPacienteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/contato-pacientes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(contatoPaciente: IContatoPaciente): Observable<EntityResponseType> {
    return this.http.post<IContatoPaciente>(this.resourceUrl, contatoPaciente, { observe: 'response' });
  }

  update(contatoPaciente: IContatoPaciente): Observable<EntityResponseType> {
    return this.http.put<IContatoPaciente>(
      `${this.resourceUrl}/${getContatoPacienteIdentifier(contatoPaciente) as number}`,
      contatoPaciente,
      { observe: 'response' }
    );
  }

  partialUpdate(contatoPaciente: IContatoPaciente): Observable<EntityResponseType> {
    return this.http.patch<IContatoPaciente>(
      `${this.resourceUrl}/${getContatoPacienteIdentifier(contatoPaciente) as number}`,
      contatoPaciente,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContatoPaciente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContatoPaciente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContatoPacienteToCollectionIfMissing(
    contatoPacienteCollection: IContatoPaciente[],
    ...contatoPacientesToCheck: (IContatoPaciente | null | undefined)[]
  ): IContatoPaciente[] {
    const contatoPacientes: IContatoPaciente[] = contatoPacientesToCheck.filter(isPresent);
    if (contatoPacientes.length > 0) {
      const contatoPacienteCollectionIdentifiers = contatoPacienteCollection.map(
        contatoPacienteItem => getContatoPacienteIdentifier(contatoPacienteItem)!
      );
      const contatoPacientesToAdd = contatoPacientes.filter(contatoPacienteItem => {
        const contatoPacienteIdentifier = getContatoPacienteIdentifier(contatoPacienteItem);
        if (contatoPacienteIdentifier == null || contatoPacienteCollectionIdentifiers.includes(contatoPacienteIdentifier)) {
          return false;
        }
        contatoPacienteCollectionIdentifiers.push(contatoPacienteIdentifier);
        return true;
      });
      return [...contatoPacientesToAdd, ...contatoPacienteCollection];
    }
    return contatoPacienteCollection;
  }
}
