import {render, screen} from '@testing-library/react';
import App from './App';

test('renders dictionary header', () => {
  render(<App/>);
  const headerElement = screen.getByText(/Dictionary/i);
  expect(headerElement).toBeInTheDocument();
});
