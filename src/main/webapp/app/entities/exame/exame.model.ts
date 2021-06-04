import * as dayjs from 'dayjs';
import { IMedico } from 'app/entities/medico/medico.model';
import { IResultadoExame } from 'app/entities/resultado-exame/resultado-exame.model';
import { IPagamentoExame } from 'app/entities/pagamento-exame/pagamento-exame.model';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';

export interface IExame {
  id?: number;
  tipoexame?: string | null;
  valorexame?: string | null;
  descontoconvenio?: string | null;
  nomemmedico?: string | null;
  prerequisito?: string | null;
  datasolicitacao?: dayjs.Dayjs | null;
  dataresultado?: dayjs.Dayjs | null;
  nomemedico?: IMedico | null;
  tipoexame?: IResultadoExame | null;
  valorexames?: IPagamentoExame[] | null;
  contaClinica?: IContaClinica | null;
}

export class Exame implements IExame {
  constructor(
    public id?: number,
    public tipoexame?: string | null,
    public valorexame?: string | null,
    public descontoconvenio?: string | null,
    public nomemmedico?: string | null,
    public prerequisito?: string | null,
    public datasolicitacao?: dayjs.Dayjs | null,
    public dataresultado?: dayjs.Dayjs | null,
    public nomemedico?: IMedico | null,
    public tipoexame?: IResultadoExame | null,
    public valorexames?: IPagamentoExame[] | null,
    public contaClinica?: IContaClinica | null
  ) {}
}

export function getExameIdentifier(exame: IExame): number | undefined {
  return exame.id;
}
