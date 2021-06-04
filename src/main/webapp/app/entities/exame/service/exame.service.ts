import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExame, getExameIdentifier } from '../exame.model';

export type EntityResponseType = HttpResponse<IExame>;
export type EntityArrayResponseType = HttpResponse<IExame[]>;

@Injectable({ providedIn: 'root' })
export class ExameService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/exames');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(exame: IExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exame);
    return this.http
      .post<IExame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exame: IExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exame);
    return this.http
      .put<IExame>(`${this.resourceUrl}/${getExameIdentifier(exame) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(exame: IExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exame);
    return this.http
      .patch<IExame>(`${this.resourceUrl}/${getExameIdentifier(exame) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExame>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExame[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExameToCollectionIfMissing(exameCollection: IExame[], ...examesToCheck: (IExame | null | undefined)[]): IExame[] {
    const exames: IExame[] = examesToCheck.filter(isPresent);
    if (exames.length > 0) {
      const exameCollectionIdentifiers = exameCollection.map(exameItem => getExameIdentifier(exameItem)!);
      const examesToAdd = exames.filter(exameItem => {
        const exameIdentifier = getExameIdentifier(exameItem);
        if (exameIdentifier == null || exameCollectionIdentifiers.includes(exameIdentifier)) {
          return false;
        }
        exameCollectionIdentifiers.push(exameIdentifier);
        return true;
      });
      return [...examesToAdd, ...exameCollection];
    }
    return exameCollection;
  }

  protected convertDateFromClient(exame: IExame): IExame {
    return Object.assign({}, exame, {
      datasolicitacao: exame.datasolicitacao?.isValid() ? exame.datasolicitacao.toJSON() : undefined,
      dataresultado: exame.dataresultado?.isValid() ? exame.dataresultado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datasolicitacao = res.body.datasolicitacao ? dayjs(res.body.datasolicitacao) : undefined;
      res.body.dataresultado = res.body.dataresultado ? dayjs(res.body.dataresultado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((exame: IExame) => {
        exame.datasolicitacao = exame.datasolicitacao ? dayjs(exame.datasolicitacao) : undefined;
        exame.dataresultado = exame.dataresultado ? dayjs(exame.dataresultado) : undefined;
      });
    }
    return res;
  }
}
