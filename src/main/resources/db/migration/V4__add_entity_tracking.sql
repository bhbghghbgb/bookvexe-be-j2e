-- Add new columns to knowledge table for entity tracking and sync

ALTER TABLE knowledge
ADD COLUMN IF NOT EXISTS entity_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS entity_type VARCHAR(50),
ADD COLUMN IF NOT EXISTS category VARCHAR(100),
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Create index for faster entity lookup
CREATE INDEX IF NOT EXISTS idx_knowledge_entity ON knowledge(entity_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_entity_type ON knowledge(entity_type);
CREATE INDEX IF NOT EXISTS idx_knowledge_category ON knowledge(category);

-- Add comments
COMMENT ON COLUMN knowledge.entity_id IS 'UUID của Trip, Route hoặc Car từ main service';
COMMENT ON COLUMN knowledge.entity_type IS 'Loại entity: TRIP, ROUTE, CAR';
COMMENT ON COLUMN knowledge.category IS 'Phân loại knowledge: TRIP_INFO, ROUTE_INFO, CAR_INFO';
COMMENT ON COLUMN knowledge.updated_at IS 'Thời điểm cập nhật knowledge';
