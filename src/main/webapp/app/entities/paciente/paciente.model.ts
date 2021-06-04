import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { IEnderecoPaciente } from 'app/entities/endereco-paciente/endereco-paciente.model';
import { IContatoPaciente } from 'app/entities/contato-paciente/contato-paciente.model';

export interface IPaciente {
  id?: number;
  cpf?: string | null;
  nome?: string | null;
  idade?: string | null;
  cpf?: IContaClinica | null;
  cpfs?: IEnderecoPaciente[] | null;
  cpfs?: IContatoPaciente[] | null;
  cpf?: IContaClinica | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public cpf?: string | null,
    public nome?: string | null,
    public idade?: string | null,
    public cpf?: IContaClinica | null,
    public cpfs?: IEnderecoPaciente[] | null,
    public cpfs?: IContatoPaciente[] | null,
    public cpf?: IContaClinica | null
  ) {}
}

export function getPacienteIdentifier(paciente: IPaciente): number | undefined {
  return paciente.id;
}
