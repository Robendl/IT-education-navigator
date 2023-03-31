export default function ToolMenu() {
  return (
    <div className="tool-menu">
      <ToolOption name="Nieuw Item" icon="post_add"/>
      <ToolOption name="Archief" icon="inventory_2"/>
      <ToolOption name="Accountbeheer" icon="manage_accounts"/>
    </div>
  );
}

function ToolOption({name, icon, action}) {
  return (
    <button>
      <span className="material-symbols-outlined tool-icon">{icon}</span>
      <span className="tool-name">{name}</span>
    </button>
  );
}